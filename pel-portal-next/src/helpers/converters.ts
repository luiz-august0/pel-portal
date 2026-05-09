import { SortingState } from '@tanstack/react-table';

export const convertSortingToSortRequest = (sorting: SortingState) => {
  return (sorting?.length ?? 0) > 0 ? `${sorting[0].id}${sorting[0].desc ? ',desc' : ',asc'}` : undefined;
};

export const convertUrlToBlob = async (url: string) => {
  const response = await fetch(url, { method: 'GET' });

  return await response.blob();
};

export const convertUrlToReadeableFile = async (url: string) => {
  const file = await convertUrlToBlob(url);

  return new Promise((resolve, reject) => {
    const fr = new FileReader();

    fr.readAsDataURL(file);

    fr.onloadend = function () {
      resolve(fr.result);
    };

    fr.onerror = function (error) {
      reject(error);
    };
  });
};
