import axios from "axios";

// 인증이 필요없는 경우
export const instance = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,
  headers: { "Content-Type": "application/json" },
});

export default instance;

// form-data 의 경우
export const fileAxios = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,
  headers: {
    "Content-Type": "multipart/form-data",
  },
});

// 인증이 필요한 경우
export const authAxios = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,
});

// 요청 전 헤더에 (엑세스) 토큰을 추가하는 인터셉터 추가
authAxios.interceptors.request.use(
  (config) => {
    const accessToken = sessionStorage.getItem("accessToken");
    config.headers.Authorization = `${accessToken}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터 추가 (리프레시 토큰 사용 로직)
authAxios.interceptors.response.use(
  (response) => {
    // 응답이 성공적으로 도착했을 경우에는 그대로 반환
    return response;
  },
  async (error) => {
    // 응답에 에러가 있을 경우에는 에러 처리
    const { response } = error;
    if (response && response.status === 401) {
      try {
        // 리프레시 토큰을 사용하여 새로운 엑세스 토큰 발급 요청
        const refreshToken = sessionStorage.getItem("refreshToken");
        const refreshResponse = await axios.get("/api/refresh", {
          headers: {
            RefreshToken: `${refreshToken}`,
          },
        });
        // 성공 -> 새로운 엑세스 토큰을 세션 스토리지에 저장
        const newAccessToken = refreshResponse.data;
        sessionStorage.setItem("accessToken", newAccessToken);
        // 이전 요청을 재시도하기 위해 새로운 엑세스 토큰을 헤더에 추가
        error.config.headers.Authorization = `${newAccessToken}`;
        // 새로운 엑세스 토큰을 사용하여 이전 요청 재시도
        return axios(error.config);
      } catch (refreshError) {
        // 리프레시 토큰도 만료된 경우
        if (refreshError.response.status === 406) {
          alert("토큰이 만료되었습니다. 다시 로그인해 주세요.");
          // 로그아웃 처리
          sessionStorage.removeItem("accessToken");
          sessionStorage.removeItem("refreshToken");
          sessionStorage.removeItem("userInfo");
          // 로그인 페이지로 이동
          window.location.href = "/login";
          // 페이지 정보 삭제
          sessionStorage.removeItem("cardId");
        }
      }
    }
    return Promise.reject(error);
  }
);
