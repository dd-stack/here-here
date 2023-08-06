import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { format } from "date-fns";

import styled from "styled-components";
import Swal from "sweetalert2";

import { getCard } from "../api/card";
import { postReceivedCard } from "../api/user";
import { postCalendar } from "../api/calendar";
import CardView from "../components/CardView";
import KakaoMap from "../components/KakaoMap";
import SnsShare from "../components/SnsShare";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 150px 20px;
  background-color: var(--background-color);
`;

const TitleText = styled.span`
  margin: 70px 0 20px 0;
  font-size: 20px;
`;

const ItemText = styled.span`
  text-align: center;
  line-height: 1.5;
`;

const KakaoMapWrapper = styled.div`
  margin-bottom: 80px;
`;

const JoinButton = styled.button`
  width: 150px;
  height: 50px;
  margin-top: 70px;
  border: 2px solid var(--second-theme-color);
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  background-color: var(--third-theme-color);
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

export default function Card() {
  const navigate = useNavigate();
  const { id } = useParams();
  sessionStorage.setItem("cardId", id);

  const userInfo = useSelector((state) => state.user?.userInfo);
  const { email } = JSON.parse(userInfo) || "";

  const [card, setCard] = useState({
    creatorEmail: "",
    title: "",
    startTime: null,
    endTime: null,
    background: "#fff",
    content: "",
    textLocation: "center",
    textColor: "#0C0A09",
    location: "",
  });

  const [calendarinfo, setCalendarinfo] = useState({
    title: "",
    time: {
      start_at: "",
      end_at: "",
      time_zone: "",
    },
    description: "",
    location: {
      name: "",
    },
  });

  const { location } = card;
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  useEffect(() => {
    // 컴포넌트가 처음 로드될 때 스크롤을 페이지 제일 위로 이동
    window.scrollTo(0, 0);
  }, []);

  useEffect(() => {
    getCard(id).then((result) => {
      if (result !== "fail") {
        setCard(result.data);
        // 날짜 포맷 변경
        const formattedStartTime = format(result.data.startTime, "yyyy년 MM월 dd일 a hh:mm");
        const formattedEndTime = format(result.data.endTime, "yyyy년 MM월 dd일 a hh:mm");
        setStartTime(formattedStartTime);
        setEndTime(formattedEndTime);
        // 톡캘린더 전송용 상태 업데이트
        setCalendarinfo({
          title: result.data.title,
          time: {
            start_at: format(result.data.startTime, "yyyy-MM-dd'T'HH:mm:00'Z'"),
            end_at: format(result.data.endTime, "yyyy-MM-dd'T'HH:mm:00'Z'"),
            time_zone: "Asia/Seoul",
          },
          description: "[여기 여기 붙어라]를 통해 등록된 일정입니다.",
          location: {
            name: result.data.location,
          },
        });
      }
      if (result === "fail") {
        alert("초대장 불러오기에 실패했습니다.");
      }
    });
  }, [id]);

  const handleClick = () => {
    if (!userInfo) {
      Swal.fire({
        title: "로그인이 필요한 서비스입니다.",
        text: "카카오로 간편하게 로그인한 후, 톡캘린더를 연동하고 받은 초대장을 관리해 보세요!",
        icon: "info",
        showCancelButton: true,
        confirmButtonColor: "var(--link-color)",
        confirmButtonText: "확인",
        cancelButtonText: "취소",
        padding: "20px 40px 40px",
      }).then((result) => {
        if (result.isConfirmed) {
          navigate("/login");
        } else {
          navigate(`/card/${id}`);
        }
      });
    } else {
      // 로그인된 상태인 경우 내가 받은 초대장 목록 업데이트 요청
      postReceivedCard(id).then((result) => {
        switch (result) {
          case "success":
            showUpdateSuccessModal();
            break;
          case "fail":
            showErrorModal("내가 받은 초대장 목록 업데이트에 실패했습니다.");
            break;
          case "409-fail":
            showErrorModal("이미 수락한 초대장입니다.");
            break;
          default:
            // 기타 에러 처리
            showErrorModal("알 수 없는 오류가 발생했습니다.");
        }
      });
    }
  };

  const showUpdateSuccessModal = () => {
    Swal.fire({
      text: "내가 받은 초대장 목록이 업데이트되었습니다. 톡캘린더에 일정을 등록하시겠습니까?",
      icon: "success",
      showCancelButton: true,
      confirmButtonColor: "var(--link-color)",
      confirmButtonText: "예",
      cancelButtonText: "아니오",
      padding: "20px 40px 40px",
    }).then((result) => {
      if (result.isConfirmed) {
        postCalendar(calendarinfo).then((result) => {
          if (result !== "fail") {
            showSuccessModal("톡캘린더에 일정이 등록되었습니다.");
          }
          if (result === "fail") {
            showErrorModal("톡캘린더 일정 등록에 실패했습니다.");
          }
        });
      }
    });
  };

  const showSuccessModal = (message) => {
    Swal.fire({
      text: message,
      icon: "success",
      confirmButtonColor: "var(--link-color)",
      confirmButtonText: "확인",
      padding: "20px 40px 40px",
    });
  };

  const showErrorModal = (message) => {
    Swal.fire({
      text: message,
      icon: "error",
      confirmButtonColor: "var(--link-color)",
      confirmButtonText: "확인",
      padding: "20px 40px 40px",
    });
  };

  return (
    <EntireContainer>
      <CardView card={card} />
      <TitleText>- 일시 -</TitleText>
      <ItemText>
        {startTime}
        <br /> ~ {endTime}
      </ItemText>
      <TitleText>- 장소 -</TitleText>
      <KakaoMapWrapper>
        <KakaoMap location={location} />
      </KakaoMapWrapper>
      {email === card.creatorEmail ? null : (
        <JoinButton type="button" onClick={handleClick}>
          수락하기
        </JoinButton>
      )}
      <SnsShare cardId={id} />
    </EntireContainer>
  );
}
