/*
 * Copyright (c) 2020-present SMC Treviso s.r.l. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.openk9.plugins.liferay.user.driver;

import io.openk9.common.api.constant.Strings;
import io.openk9.osgi.util.AutoCloseables;
import io.openk9.plugin.driver.manager.api.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;

@Component(
	immediate = true,
	service = DocumentTypeFactoryRegistryAware.class
)
public class DocumentTypeDefinition implements
	DocumentTypeFactoryRegistryAware {

	@Override
	public AutoCloseables.AutoCloseableSafe apply(
		DocumentTypeFactoryRegistry documentTypeFactoryRegistry) {

		String pluginDriverName = _pluginDriver.getName();

		return documentTypeFactoryRegistry
			.register(
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, true,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("user")
						.searchKeywords(
							List.of(
								SearchKeyword.number("userId", "user"),
								SearchKeyword.boostText("screenName", "user", 5),
								SearchKeyword.boostText("emailAddress", "user", 5),
								SearchKeyword.number("employeeNumber", "user"),
								SearchKeyword.text("jobTitle", "user"),
								SearchKeyword.text("jobClass", "user"),
								SearchKeyword.number("male", "user"),
								SearchKeyword.text("twitterSn", "user"),
								SearchKeyword.text("skypeSn", "user"),
								SearchKeyword.text("facebookSn", "user"),
								SearchKeyword.boostText("firstName", "user", 5),
								SearchKeyword.boostText("middleName", "user", 5),
								SearchKeyword.boostText("lastName", "user", 5),
								SearchKeyword.number("birthday", "user")
							)
						)
						.sourceFields(
							List.of(
								Field.of("userId", FieldType.LONG),
								Field.of("screenName", FieldType.TEXT),
								Field.of(
									"emailAddress", FieldType.TEXT,
									Collections.singletonMap("analyzer", "email")),
								Field.of("employeeNumber", FieldType.TEXT),
								Field.of("jobTitle", FieldType.TEXT),
								Field.of("jobClass", FieldType.TEXT),
								Field.of("male", FieldType.BOOLEAN),
								Field.of("twitterSn", FieldType.TEXT),
								Field.of("skypeSn", FieldType.TEXT),
								Field.of("facebookSn", FieldType.TEXT),
								Field.of("firstName", FieldType.TEXT),
								Field.of("middleName", FieldType.TEXT),
								Field.of("lastName", FieldType.TEXT),
								Field.of("birthday", FieldType.LONG)
							)
						)
						.build()
				)
			);
	}

	@Reference(
		target = "(component.name=io.openk9.plugins.liferay.driver.LiferayPluginDriver)"
	)
	private PluginDriver _pluginDriver;

}
