import { authAxios } from "./core/instance";

export const postCalendar = async (calendarInfo) => {
  try {
    const response = await authAxios.post("/calendar/event", calendarInfo);
    return response;
  } catch (error) {
    console.log(error);
    return "fail";
  }
};
