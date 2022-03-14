import React from "react";
import { GenericResultItem } from "@openk9/rest-api";
import { UserResultItem } from "./UserItem";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { rendererComponents } from "@openk9/search-frontend";

const {
  ResultContainer,
  ResultTitle,
  HighlightableText,
  ResultAttribute,
} = rendererComponents;

type UserResultProps = { result: GenericResultItem<UserResultItem> };
export function UserResult({ result }: UserResultProps) {
  return (
    <ResultContainer icon={<FontAwesomeIcon icon={faUser} />}>
      <ResultTitle>
        <HighlightableText result={result} path="user.fullName" />
      </ResultTitle>
      {result.source.user.jobTitle && (
        <ResultAttribute label="Job">
          <HighlightableText result={result} path="user.jobTitle" />
        </ResultAttribute>
      )}
      {result.source.user.emailAddress && (
        <ResultAttribute label="Email">
          <HighlightableText result={result} path="user.emailAddress" />
        </ResultAttribute>
      )}
      {result.source.user.phoneNumber && (
        <ResultAttribute label="Phone">
          <HighlightableText result={result} path="user.phoneNumber" />
        </ResultAttribute>
      )}
    </ResultContainer>
  );
}
