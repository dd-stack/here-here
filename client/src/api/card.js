import { authAxios } from "./core/instance";

export const postCard = async (card) => {
  try {
    const response = await authAxios.post("/card", card);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
