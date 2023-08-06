import axios from "./core/instance";
import { authAxios } from "./core/instance";

export const postCard = async (card) => {
  try {
    const response = await authAxios.post("/making/card", card);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const getCard = async (id) => {
  try {
    const response = await axios.get(`/making/getcard?cardId=${id}`);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const deleteCard = async (id) => {
  try {
    await authAxios.delete(`/making/deletecard?cardId=${id}`);
    return "success";
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
