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

// 요청 전 헤더에 토큰을 추가하는 인터셉터 추가
// next stage: 인터셉터 추가로 공부해 보기.
// 왜 그냥 헤더에 넣었을 땐 토큰 상태가 최신화되지 못했을까?
authAxios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    config.headers.Authorization = token;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// useSelector는 컴포넌트 내부에서만 사용할 수 있으므로
// 만들어둔 리덕스 상태값을 사용할 수 없었다.
// refactor point: 다른 방법이 더 있는지 찾아보기
