import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { format } from "date-fns";

import styled from "styled-components";
import Swal from "sweetalert2";

import { getCard } from "../api/card";
import { postReceivedCard } from "../api/user";
import { postCalendar } from "../api/calendar";
import { deleteReceivedCard } from "../api/user";
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
  // 다시 돌아오기 위해 카드 id 저장
  sessionStorage.setItem("cardId", id);

  const userInfo = useSelector((state) => state.user?.userInfo);
  const { email } = JSON.parse(userInfo) || "";

  const [card, setCard] = useState({
    creatorEmail: "",
    title: "",
    startTime: "",
    endTime: "",
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
  // 포맷 변경된 화면 표시용 날짜
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
        const formattedStartTime = format(
          new Date(result.data.startTime),
          "yyyy년 MM월 dd일 a hh:mm"
        );
        const formattedEndTime = format(new Date(result.data.endTime), "yyyy년 MM월 dd일 a hh:mm");
        setStartTime(formattedStartTime);
        setEndTime(formattedEndTime);
        // 톡캘린더 전송용 상태 업데이트
        setCalendarinfo({
          title: result.data.title,
          time: {
            start_at: result.data.startTime,
            end_at: result.data.endTime,
            time_zone: "UTC*",
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
            showErrorModal(
              "알 수 없는 오류가 발생했습니다. 자세한 내용은 사이트 관리자에게 문의해 주시기 바랍니다."
            );
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
          switch (result) {
            case "success":
              showSuccessModal("톡캘린더에 일정이 등록되었습니다.");
              break;
            case "fail":
              showErrorModal("톡캘린더 일정 등록에 실패했습니다.");
              break;
            case "402-fail":
              Swal.fire({
                text: "톡캘린더 접근 권한 동의가 필요합니다. 동의하시겠습니까? (동의 후, 다시 수락하기 버튼을 눌러주세요.)",
                icon: "info",
                showCancelButton: true,
                confirmButtonColor: "var(--link-color)",
                confirmButtonText: "예",
                cancelButtonText: "아니오",
                padding: "20px 40px 40px",
              }).then((result) => {
                if (result.isConfirmed) {
                  deleteReceivedCard(id).then((result) => {
                    if (result === "fail") {
                      Swal.fire({
                        text: "내가 받은 초대장 목록에서 이 초대장을 삭제해 주셔야 다시 수락하기 버튼을 누를 수 있습니다.",
                        icon: "info",
                        confirmButtonColor: "var(--link-color)",
                        confirmButtonText: "확인",
                        padding: "20px 40px 40px",
                      });
                    }
                    window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.REACT_APP_REST_API_KEY}&redirect_uri=${process.env.REACT_APP_REDIRECT_URI}&response_type=code&scope=talk_calendar`;
                  });
                }
              });
              break;
            default:
              // 기타 에러 처리
              showErrorModal(
                "알 수 없는 오류가 발생했습니다. 자세한 내용은 사이트 관리자에게 문의해 주시기 바랍니다."
              );
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
