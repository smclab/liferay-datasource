export type DmlResultItem = {
  document: {
    title: string;
    lastModifiedDate: string;
    contentType: string;
    URL: string;
    content: string;
    path: string;
    previewURLs: Array<string>;
    previewUrl: string;
    category: string;
    spaceId: string;
    spaceName: string;
  };
}
