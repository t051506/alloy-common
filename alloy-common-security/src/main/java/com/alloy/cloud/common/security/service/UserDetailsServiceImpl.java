/*
 *
 *  *  Copyright (c) 2019-2020, tn_kec (tankechao@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.alloy.cloud.common.security.service;

import com.alloy.cloud.common.core.base.R;
import com.alloy.cloud.common.core.constant.CacheConstants;
import com.alloy.cloud.common.core.constant.CommonConstants;
import com.alloy.cloud.common.core.constant.SecurityConstants;
import com.alloy.cloud.common.core.util.SpringContextUtils;
import com.alloy.cloud.ucenter.api.dto.RemoteUser;
import com.alloy.cloud.ucenter.api.feign.RemoteUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详细信息
 *
 * @author tn_kec
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RemoteUserService remoteUserService;

    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {

        RedisTemplate redisTemplate = SpringContextUtils.getBean(RedisTemplate.class);

        Object cache = redisTemplate.opsForValue().get(CacheConstants.USER_DETAILS + username);

        if (cache != null) {
            return (CloudUser) cache;
        }
        R<RemoteUser> result = remoteUserService.loadByUsername(SecurityConstants.FROM_IN, username);
        UserDetails userDetails = getUserDetails(result);
        redisTemplate.opsForValue().set(CacheConstants.USER_DETAILS + username,userDetails);
        return userDetails;
    }

    /**
     * 构建userdetails
     * @param result 用户信息
     * @return
     */
	private UserDetails getUserDetails(R<RemoteUser> result) {
		if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

        RemoteUser user = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();
//		if (ArrayUtil.isNotEmpty(info.getRoles())) {
//			// 获取角色
//			Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
//			// 获取资源
//			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
//        }
//        SysUser user = info.getSysUser();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
				.createAuthorityList(dbAuthsSet.toArray(new String[0]));

		// 构造security用户
		return new CloudUser(user.getOrgCode(), user.getUsername(),
				SecurityConstants.BCRYPT + user.getPassword(),
				user.getIsLock().equals(CommonConstants.STATUS_NORMAL), true, true, true, authorities);
	}

}
