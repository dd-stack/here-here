import { fileAxios } from "./core/instance";

export const postImage = async (formData) => {
  try {
    const response = await fileAxios.post("/image", formData);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

// export const deleteImage = async (url) => {
//   try {
//     await fileAxios.delete("/image", { data: { image: url } });
//     return "success";
//   } catch (error) {
//     console.log(error);
//     return "fail";
//   }
// };
