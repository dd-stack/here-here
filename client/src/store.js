import { configureStore, createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: { userInfo: sessionStorage.getItem("userInfo") || null },
  reducers: {
    setUserInfo(state, action) {
      state.userInfo = action.payload;
      sessionStorage.setItem("userInfo", action.payload);
    },
    clearUserInfo(state) {
      state.userInfo = null;
      sessionStorage.removeItem("userInfo");
    },
  },
});

export default configureStore({
  reducer: {
    user: userSlice.reducer,
  },
});

export const { setUserInfo, clearUserInfo } = userSlice.actions;
