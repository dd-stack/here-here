import { configureStore, createSlice } from "@reduxjs/toolkit";

import setDataWithExpiry from "./utils/setDataWithExpiry";

// 엑세스 토큰(유효시간 2시간) + 유저 정보
// refactor point: 유효 시간(만료 시간)을 토큰 디코딩으로 가져오면 더 좋을 듯
// 새로고침해도 사라지지 않음. -> 이 로직이 계속 필요할 것 같으면,
// redux-persist 라는 라이브러리도 있음.
// refactor point: 리프레시 토큰도 사용해 보기
const userSlice = createSlice({
  name: "user",
  initialState: {
    token: localStorage.getItem("token") || "",
    userInfo: JSON.parse(localStorage.getItem("userInfo")) || null,
  },
  reducers: {
    setUserInfo(state, action) {
      state.userInfo = action.payload;
      setDataWithExpiry("userInfo", action.payload, 2);
    },
    clearUserInfo(state) {
      state.userInfo = null;
      localStorage.removeItem("userInfo");
    },
    setToken(state, action) {
      state.token = action.payload;
      setDataWithExpiry("token", action.payload, 2);
    },
    clearToken(state) {
      state.token = "";
      localStorage.removeItem("token");
    },
  },
});

export default configureStore({
  reducer: {
    userSlice: userSlice.reducer,
  },
});

export const { setUserInfo, clearUserInfo, setToken, clearToken } = userSlice.actions;
