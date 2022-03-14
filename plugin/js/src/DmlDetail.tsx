import React from "react";
import { GenericResultItem } from "@openk9/rest-api";
import { DmlResultItem } from "./DmlItem";
import { faFileAlt } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { rendererComponents } from "@openk9/search-frontend";

const {
  DetailContainer,
  DetailIconContainer,
  DetailLink,
  DetailTextContent,
  DetailTitle,
  HighlightableText,
  DetailAttribute
} = rendererComponents;

type PreviewSliderProps = {
  images: Array<string>;
};
function PreviewSlider({ images }: PreviewSliderProps) {
  const [selectedIndex, setSelectedIndex] = React.useState(0);
  return (
    <div>
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <a
          href="javascript:void(0)"
          onClick={() => setSelectedIndex(Math.max(0, selectedIndex - 1))}
        >
          {"<"}
        </a>
        &nbsp;
        {new Array(images.length).fill(0).map((_, index, array) => {
          return (
            <React.Fragment key={index}>
              <a
                href={index === selectedIndex ? undefined : "javascript:void(0)"}
                onClick={(event) => {
                  event.preventDefault();
                  setSelectedIndex(index);
                }}
              >
                {index + 1}
              </a>
              {index < array.length - 1 && " - "}
            </React.Fragment>
          );
        })}
        &nbsp;
        <a
          href="javascript:void(0)"
          onClick={() =>
            setSelectedIndex(Math.min(images.length - 1, selectedIndex + 1))
          }
        >
          {">"}
        </a>
      </div>
      <div
        style={{
          width: "100%",
          height: "50vh",
          backgroundColor: "white",
          borderRadius: "4px",
          border: "1px solid var(--openk9-embeddable-search--border-color)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        {images[selectedIndex] && (
          <img
            src={images[selectedIndex]}
            alt="preview"
            style={{ maxWidth: "100%", maxHeight: "100%" }}
          />
        )}
      </div>
    </div>
  );
}

type DocumentDetailProps = {
  result: GenericResultItem<DmlResultItem>;
};
export function DmlDetail({ result }: DmlDetailProps) {
  return (
    <DetailContainer>
      <DetailIconContainer>
        <FontAwesomeIcon icon={faFileAlt} />
      </DetailIconContainer>
      <DetailTitle>
        <HighlightableText result={result} path="document.title" />
      </DetailTitle>
      <DetailLink href={result.source.document.URL}>
        <HighlightableText result={result} path="document.URL" />
      </DetailLink>
      <DetailAttribute label="Space Id">
            <HighlightableText result={result} path="spaces.spaceId" />
        </DetailAttribute>
        <DetailAttribute label="Space Name">
            <HighlightableText result={result} path="spaces.spaceName" />
          </DetailAttribute>
      <PreviewSlider images={result.source.document.previewURLs}/>
    </DetailContainer>
  );
}
