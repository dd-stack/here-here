import axios from "./core/instance";

// 카카오 로그인
export const login = async () => {
  try {
    const response = await axios.get("/oauth2/authorization/kakao");
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

// todo: 로그아웃도 통신 필요?
