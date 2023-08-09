import { authAxios } from "./core/instance";

export const postCalendar = async (calendarInfo) => {
  try {
    await authAxios.post("/calendar/event", calendarInfo);
    return "success";
  } catch (error) {
    console.log(error);
    return error.response.status === 402 ? "402-fail" : "fail";
  }
};
