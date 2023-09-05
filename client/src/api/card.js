import axios from "./core/instance";
import { authAxios } from "./core/instance";

export const postCard = async (card) => {
  try {
    const response = await authAxios.post("/card/createCard", card);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const getCard = async (id) => {
  try {
    const response = await axios.get(`/card/getCard?cardId=${id}`);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const deleteCard = async (id) => {
  try {
    await authAxios.delete(`/card/deleteCard?cardId=${id}`);
    return "success";
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const getParticipant = async (id) => {
  try {
    const response = await authAxios.get(`/invitation/checkMember/${id}`);
    return response;
  } catch (error) {
    console.log(error);
    return error.response.status === 402 ? "402-fail" : "fail";
  }
};
