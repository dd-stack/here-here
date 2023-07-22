import axios from "./core/instance";
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

export const getCard = async () => {
  try {
    const response = await axios.get("/card");
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
