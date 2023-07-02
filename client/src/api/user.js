import axios from "./core/instance";

// 카카오 로그인 (임의로 /login으로 get요청)
export const login = async () => {
  try {
    const response = await axios.get("/login");
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};

// todo: 로그아웃도 통신 필요?
