import axios from "./core/instance";

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

// todo: 회원탈퇴 기능 구현
