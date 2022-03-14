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

package io.openk9.plugins.liferay.calendar.driver;

import io.openk9.common.api.constant.Strings;
import io.openk9.osgi.util.AutoCloseables;
import io.openk9.plugin.driver.manager.api.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
					pluginDriverName, false,
					DocumentType
						.builder()
						.icon(Strings.BLANK)
						.name("calendar")
						.searchKeywords(
							List.of(
								SearchKeyword.number("calendarBookingId", "calendar"),
								SearchKeyword.text("description", "calendar"),
								SearchKeyword.text("location", "calendar"),
								SearchKeyword.text("title", "calendar"),
								SearchKeyword.text("titleCurrentValue", "calendar"),
								SearchKeyword.number("startTime", "calendar"),
								SearchKeyword.number("endTime", "calendar"),
								SearchKeyword.number("allDay", "calendar")
							)
						)
						.sourceFields(
							List.of(
								Field.of("calendarBookingId", FieldType.LONG),
								Field.of("description", FieldType.TEXT),
								Field.of("location", FieldType.TEXT),
								Field.of("title", FieldType.TEXT),
								Field.of("titleCurrentValue", FieldType.TEXT),
								Field.of(
										"startTime", FieldType.TEXT, Map.of(
												"fields", Map.of(
														"sortable", Map.of(
																"type", FieldType.DATE.getType(),
																"format", "MM-dd-yyyy HH:mm:ss||yyyy-MM-dd||epoch_millis",
																"ignore_malformed", true
														),
														"keyword", Map.of(
																"type", FieldType.KEYWORD.getType()
														)
												)
										)
								),
								Field.of(
										"endTime", FieldType.TEXT, Map.of(
												"fields", Map.of(
														"sortable", Map.of(
																"type", FieldType.DATE.getType(),
																"format", "MM-dd-yyyy HH:mm:ss||yyyy-MM-dd||epoch_millis",
																"ignore_malformed", true
														),
														"keyword", Map.of(
																"type", FieldType.KEYWORD.getType()
														)
												)
										)
								),
								Field.of("allDay", FieldType.BOOLEAN)
							)
						)
						.build()
				)
			);
	}

	@Reference(
		target = "(component.name=io.openk9.plugins.liferay.calendar.driver.CalendarPluginDriver)"
	)
	private PluginDriver _pluginDriver;

}
