import { authAxios } from "./core/instance";

export const postCard = async (card) => {
  try {
    await authAxios.post("/card", card);
    return "success";
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
