import React from "react";
import { GenericResultItem } from "@openk9/rest-api";
import { DmlResultItem } from "./DmlItem";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFileAlt } from "@fortawesome/free-solid-svg-icons";
import { rendererComponents } from "@openk9/search-frontend";

const {
  ResultTitle,
  ResultContainer,
  HighlightableText,
  ResultLink,
  ResultTextContent,
  BadgeContainer,
  Badge
} = rendererComponents;

type DmlResultProps = { result: GenericResultItem<DmlResultItem> };
export function DmlResult({ result }: DmlResultProps) {
  return (
    <ResultContainer icon={<FontAwesomeIcon icon={faFileAlt} />}>
      <ResultTitle>
        <HighlightableText result={result} path="document.title" />
      </ResultTitle>
      <ResultLink href={result.source.document.url}>
        <HighlightableText result={result} path="document.url" />
      </ResultLink>
      <BadgeContainer>
          {result.source.document.category && (
            <Badge>{result.source.document.category}</Badge>
          )}
        </BadgeContainer>
      <ResultTextContent result={result} path="document.content" />
    </ResultContainer>
  );
}
