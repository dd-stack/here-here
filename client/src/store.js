import { configureStore, createSlice } from "@reduxjs/toolkit";

// 유저 정보
let userInfo = createSlice({
  name: "userInfo",
  initialState: {},
  reducers: {
    setUserInfo(state, action) {
      return action.payload;
    },
  },
});

// 엑세스 토큰
let accessToken = createSlice({
  name: "accessToken",
  initialState: "",
  reducers: {
    setAccessToken(state, action) {
      return action.payload;
    },
  },
});

// 상태 내보내기
export default configureStore({
  reducer: {
    userInfo: userInfo.reducer,
    accessToken: accessToken.reducer,
  },
});

// 상태 변경 함수 내보내기
export let { setUserInfo } = userInfo.actions;
export let { setAccessToken } = accessToken.actions;
