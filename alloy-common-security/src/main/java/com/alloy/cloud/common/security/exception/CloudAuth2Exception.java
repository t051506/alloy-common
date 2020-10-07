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

package com.alloy.cloud.common.security.exception;

import com.alloy.cloud.common.security.component.CloudAuth2ExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author tn_kec
 * @since 2019/2/1 自定义OAuth2Exception
 */
@JsonSerialize(using = CloudAuth2ExceptionSerializer.class)
public class CloudAuth2Exception extends OAuth2Exception {

	@Getter
	private String errorCode;

	public CloudAuth2Exception(String msg) {
		super(msg);
	}

	public CloudAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

}
