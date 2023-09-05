import axios from "./core/instance";
import { authAxios } from "./core/instance";

export const getUserInfo = async (code) => {
  try {
    const response = await axios.post("/api/token", { code });
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const logout = async () => {
  try {
    await authAxios.post("/member/logout");
  } catch (error) {
    console.log(error);
  }
};

export const deleteUser = async () => {
  try {
    await authAxios.delete("/member/delete");
    return "success";
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const getSentCards = async (page, size) => {
  try {
    const response = await authAxios.get(`member/mypage/createCards?page=${page}&size=${size}`);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const getReceivedCards = async (page, size) => {
  try {
    const response = await authAxios.get(`member/mypage/receiveCards?page=${page}&size=${size}`);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const postReceivedCard = async (id) => {
  try {
    await authAxios.post(`/invitation/accept/${id}`);
    return "success";
  } catch (error) {
    console.log(error);
    return error.response.status === 409 ? "409-fail" : "fail";
  }
};

export const deleteReceivedCard = async (id) => {
  try {
    await authAxios.delete(`/invitation/delete/${id}`);
    return "success";
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
