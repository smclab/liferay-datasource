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

package io.openk9.plugins.liferay.dml.driver;

import io.openk9.common.api.constant.Strings;
import io.openk9.osgi.util.AutoCloseables;
import io.openk9.plugin.driver.manager.api.DocumentType;
import io.openk9.plugin.driver.manager.api.DocumentTypeFactory;
import io.openk9.plugin.driver.manager.api.DocumentTypeFactoryRegistry;
import io.openk9.plugin.driver.manager.api.DocumentTypeFactoryRegistryAware;
import io.openk9.plugin.driver.manager.api.Field;
import io.openk9.plugin.driver.manager.api.FieldType;
import io.openk9.plugin.driver.manager.api.PluginDriver;
import io.openk9.plugin.driver.manager.api.SearchKeyword;
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
						.searchKeywords(
							List.of(
								SearchKeyword.text("path", "file")
							)
						)
						.name("file")
						.icon(Strings.BLANK)
						.sourceFields(
							List.of(
								Field.of("lastModifiedDate", FieldType.DATE),
								Field.of("path", FieldType.TEXT)
							)
						)
						.build()
				),
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, false,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("document")
						.searchKeywords(
							List.of(
								SearchKeyword.text("content", "document"),
								SearchKeyword.text("title", "document")
							)
						)
						.sourceFields(
							List.of(
								Field.of("previewURLs", FieldType.TEXT),
								Field.of("previewUrl", FieldType.TEXT),
								Field.of("content", FieldType.TEXT),
								Field.of("contentType", FieldType.TEXT),
								Field.of("title", FieldType.TEXT),
								Field.of("URL", FieldType.TEXT)
							)
						)
						.build()
				),
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, false,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("office-word")
						.searchKeywords(List.of())
						.build()
				),
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, false,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("office-excel")
						.searchKeywords(List.of())
						.build()
				),
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, false,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("office-powerpoint")
						.searchKeywords(List.of())
						.build()
				),
				DocumentTypeFactory.DefaultDocumentTypeFactory.of(
					pluginDriverName, false,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("acl")
						.searchKeywords(List.of())
						.sourceFields(
							List.of(
								Field.of(
									"allow", Field.of(
										"roles", 
										FieldType.KEYWORD
										)
									)
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
