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

// todo: 로그아웃도 통신 필요?

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
    const response = await authAxios.get(`member/mypage/createcard?page=${page}&size=${size}`);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

export const getReceivedCards = async (page, size) => {
  try {
    const response = await authAxios.get(`member/mypage/receivedcard?page=${page}&size=${size}`);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
