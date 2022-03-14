import React from "react";
import { Plugin } from "@openk9/rest-api";
import { DmlItem } from "./DmlItem";
import { DmlResult } from "./DmlResult";
import { DmlDetail } from "./DmlDetail";
import { CalendarResult } from "./CalendarResult";
import { CalendarDetail } from "./CalendarDetail";
import { UserDetail } from "./UserDetail";
import { UserResult } from "./UserResult";

export const plugin: Plugin<DmlResultItem> = {
  pluginId: "liferay-datasource",
  displayName: "Liferay DataSource",
  pluginServices: [
    {
      type: "DATASOURCE",
      displayName: "Dml DataSource",
      driverServiceName: "io.openk9.plugins.liferay.dml.driver.DmlPluginDriver",
      initialSettings: `
        {
            "domain": "https://liferay.openk9.io",
            "username": "test@liferay.com",
            "password": "test"
        }
      `,
    },
    {
      type: "DATASOURCE",
      displayName: "Calendar DataSource",
      driverServiceName: "io.openk9.plugins.liferay.calendar.driver.CalendarPluginDriver",
      initialSettings: `
        {
            "domain": "https://liferay.openk9.io",
            "username": "test@liferay.com",
            "password": "test"
        }
      `,
    },
    {
      type: "DATASOURCE",
      displayName: "User DataSource",
      driverServiceName: "io.openk9.plugins.liferay.user.driver.UserPluginDriver",
      initialSettings: `
        {
            "domain": "https://liferay.openk9.io",
            "username": "test@liferay.com",
            "password": "test"
        }
      `,
    },
    {
      type: "ENRICH",
      displayName: "Liferay NER",
      serviceName:
        "io.openk9.plugins.liferay.enrichprocessor.AsyncLiferayNerEnrichProcessor",
      initialSettings: `{"entities": ["person", "email","organization"], "confidence": 0.90}`,
    },
    {
      type: "RESULT_RENDERER",
      resultType: "dml",
      resultRenderer: DmlResult,
      sidebarRenderer: DmlDetail,
    },
    {
      type: "RESULT_RENDERER",
      resultType: "calendar",
      resultRenderer: CalendarResult,
      sidebarRenderer: CalendarDetail,
    },
    {
      type: "RESULT_RENDERER",
      resultType: "user",
      resultRenderer: UserResult,
      sidebarRenderer: UserDetail,
    }
  ],
};
