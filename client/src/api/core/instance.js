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

// 인증이 필요한 경우 -> 그때그때 헤더에 토큰 추가하기
export const authAxios = axios.create({
  baseURL: `${process.env.REACT_APP_BASE_URL}`,
});

// useSelector는 컴포넌트 내부에서만 사용할 수 있으므로
// 전역 상태 관련 요청들은 컴포넌트 내부에서 하는 걸로 한다.
// 토큰을 미리 헤더에 넣지 못하는 것도 같은 이유.
// refactor point: 다른 방법이 더 있을지 찾아보기
