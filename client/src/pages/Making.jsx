import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import DaumPostcode from "react-daum-postcode";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { ko } from "date-fns/esm/locale";
import { format } from "date-fns";

import styled from "styled-components";
import Swal from "sweetalert2";
import { FcCameraIdentification } from "react-icons/fc";

import { postImage, deleteImage } from "../api/image";
import { postCard } from "../api/card";
import CardView from "../components/CardView";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const CardContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 0 60px 0;
  gap: 20px;
  > span {
    color: var(--gray-color);
  }
`;

const InputWrapper = styled.div`
  display: flex;
  align-items: center;
  width: 330px;
  padding: 20px 0;
  gap: 10px;
  > label {
    white-space: nowrap;
  }
`;

const InputItems = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
`;

const ColorBox = styled.div`
  height: 50px;
  width: 50px;
  border: 1px solid var(--gray-color);
  background-color: ${(props) => props.color};
  cursor: pointer;
`;

const ColorInput = styled.input`
  height: 50px;
  width: 50px;
  cursor: pointer;
`;

const ImageInput = styled.div`
  > label {
    cursor: pointer;
  }
  > input {
    display: none;
  }
`;

const Textarea = styled.textarea`
  height: 100px;
  min-width: 200px;
  resize: vertical;
`;

const TextLocationBox = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
  border: 1px solid var(--gray-color);
  cursor: pointer;
`;

const SubmitButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
  width: 330px;
  margin: 60px 0 20px 0;
  > button {
    width: 150px;
    height: 50px;
    border: 2px solid var(--second-theme-color);
    border-radius: 10px;
    font-size: 15px;
    font-weight: 600;
    background-color: var(--third-theme-color);
    box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
      0 2px 8px hsla(0, 0%, 0%, 0.05);
  }
`;

export default function Making() {
  const navigate = useNavigate();

  const isLogin = useSelector((state) => state.user?.userInfo);

  // 로그인이 되어 있지 않다면 로그인 화면으로
  useEffect(() => {
    if (!isLogin) {
      navigate("/login");
    }
  }, [isLogin, navigate]);

  const [card, setCard] = useState({
    title: "",
    startTime: null,
    endTime: null,
    background: "#fff",
    content: "",
    textLocation: "center",
    textColor: "#0C0A09",
    location: "",
  });
  const { title, startTime, endTime, background, content, textColor, location } = card;
  const [disabled, setDisabled] = useState(false);
  const [openPostcode, setOpenPostcode] = useState(false);

  const handleInputChange = (key, value) => {
    setCard((previous) => ({ ...previous, [key]: value }));
    // 시작 날짜가 변경되면 종료 날짜를 초기화
    if (key === "startTime") {
      setCard((previous) => ({ ...previous, endTime: null }));
    }
  };

  const handleColorChange = (color) => {
    // 이미 이미지 url이 들어 있다면 해당 이미지 삭제 후 색으로 변경
    if (!background.startsWith("#")) {
      deleteImage(background);
    }
    setCard((previous) => ({ ...previous, background: color }));
  };

  const handleImageChange = (event) => {
    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    // 이미 이미지 url이 들어 있다면 해당 이미지 삭제 후 업로드 요청
    if (!background.startsWith("#")) {
      deleteImage(background);
    }
    postImage(formData).then((result) => {
      if (result !== "fail") {
        setCard((previous) => ({ ...previous, background: result.data }));
      }
      if (result === "fail") {
        alert("이미지 등록에 실패했습니다.");
      }
    });
  };

  const handleSearchButtonClick = () => {
    setOpenPostcode((current) => !current);
  };

  const handleSelectAddress = (data) => {
    setCard((previous) => ({ ...previous, location: data.address }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setDisabled(true);

    // Date 객체를 UTC, RFC5545의 DATE-TIME 형식으로 변환
    const formattedStartTime = format(startTime, "yyyy-MM-dd'T'HH:mm:ss");
    const formattedEndTime = format(endTime, "yyyy-MM-dd'T'HH:mm:ss");
    const updatedCard = {
      ...card,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
    };

    Swal.fire({
      text: "초대장을 만드는 중입니다.",
      showConfirmButton: false,
      timer: 1200,
      timerProgressBar: true,
      padding: "20px 40px 40px",
    });

    setTimeout(() => {
      postCard(updatedCard).then((result) => {
        if (result !== "fail") {
          const cardId = result.data.id;
          navigate(`/card/${cardId}`);
          setDisabled(false);
        }
        if (result === "fail") {
          alert("초대장 만들기에 실패했습니다.");
          setDisabled(false);
        }
      });
    }, 1200);
  };

  const labels = [
    {
      id: "title",
      title: "초대장 제목 :",
      children: (
        <input
          id="title"
          type="text"
          placeholder="캘린더 등록 시 보이는 문구입니다."
          maxLength="20"
          value={title}
          onChange={(event) => handleInputChange("title", event.target.value)}
          required
        />
      ),
    },
    {
      id: "startTime",
      title: "시작 날짜 :",
      children: (
        <DatePicker
          id="startTime"
          locale={ko}
          placeholderText="시작 날짜/시간 선택"
          dateFormat="yyyy-MM-dd HH:mm" // 날짜 형식
          selected={startTime} // value 값
          onChange={(value) => handleInputChange("startTime", value)}
          showTimeSelect
          timeFormat="HH:mm"
          timeIntervals={10} // 10분 간격으로 시간 선택 가능
          timeCaption="시간"
          minDate={new Date()} // 현재 시간 이후만 선택 가능
          filterTime={(time) => {
            return new Date().getTime() < new Date(time).getTime();
          }} // 현재 시간 이후만 선택 가능
          closeOnScroll={true} // 스크롤을 움직였을 때 자동으로 닫히도록 설정
          required
        />
      ),
    },
    {
      id: "endTime",
      title: "종료 날짜 :",
      children: (
        <DatePicker
          id="endTime"
          locale={ko}
          placeholderText="종료 날짜/시간 선택"
          dateFormat="yyyy-MM-dd HH:mm" // 날짜 형식
          selected={endTime} // value 값
          onChange={(value) => handleInputChange("endTime", value)}
          showTimeSelect
          timeFormat="HH:mm"
          timeIntervals={10} // 10분 간격으로 시간 선택 가능
          timeCaption="시간"
          minDate={startTime} // 시작 날짜 이후만 선택 가능
          filterTime={(time) => {
            return startTime.getTime() < new Date(time).getTime();
          }} // 시작 시간 10분 이후만 선택 가능
          closeOnScroll={true} // 스크롤을 움직였을 때 자동으로 닫히도록 설정
          disabled={!startTime} // 시작 날짜가 선택되지 않은 경우에는 비활성화
          required
        />
      ),
    },
    {
      id: "background",
      title: "배경 :",
      children: (
        <>
          <ColorBox color="#F1CBD2" onClick={() => handleColorChange("#F1CBD2")} />
          <ColorBox color="#F2C9B7" onClick={() => handleColorChange("#F2C9B7")} />
          <ColorBox color="#EFDA92" onClick={() => handleColorChange("#EFDA92")} />
          <ColorBox color="#B5D9CD" onClick={() => handleColorChange("#B5D9CD")} />
          <ColorBox color="#C3D0D7" onClick={() => handleColorChange("#C3D0D7")} />
          <ColorBox color="#B2B2CD" onClick={() => handleColorChange("#B2B2CD")} />
          <ColorInput
            id="background"
            type="color"
            value={background}
            onChange={(event) => handleInputChange("background", event.target.value)}
          />
          <ImageInput>
            <label htmlFor="imageInput">
              <FcCameraIdentification size="50px" />
            </label>
            <input id="imageInput" type="file" accept="image/*" onChange={handleImageChange} />
          </ImageInput>
        </>
      ),
    },
    {
      id: "content",
      title: "상세 내용 :",
      children: (
        <Textarea
          id="content"
          type="text"
          placeholder="일정, 준비물, 초대 문구 등 상세 내용을 적어주세요. (최대 150자)"
          maxLength="150"
          value={content}
          onChange={(event) => handleInputChange("content", event.target.value)}
        />
      ),
    },
    {
      id: "textLocation",
      title: "텍스트 위치 :",
      children: (
        <>
          <TextLocationBox onClick={() => handleInputChange("textLocation", "top")}>
            위
          </TextLocationBox>
          <TextLocationBox onClick={() => handleInputChange("textLocation", "center")}>
            중간
          </TextLocationBox>
          <TextLocationBox onClick={() => handleInputChange("textLocation", "bottom")}>
            아래
          </TextLocationBox>
        </>
      ),
    },
    {
      id: "textColor",
      title: "텍스트 색 :",
      children: (
        <>
          <ColorBox color="#fdfcfa" onClick={() => handleInputChange("textColor", "#fdfcfa")} />
          <ColorBox color="#9E9E9E" onClick={() => handleInputChange("textColor", "#9E9E9E")} />
          <ColorBox color="#0C0A09" onClick={() => handleInputChange("textColor", "#0C0A09")} />
          <ColorInput
            id="textColor"
            type="color"
            value={textColor}
            onChange={(event) => handleInputChange("textColor", event.target.value)}
          />
        </>
      ),
    },
    {
      id: "location",
      title: "장소 :",
      children: (
        <>
          <input
            id="location"
            type="text"
            placeholder="주소를 검색해 주세요."
            value={location}
            onClick={handleSearchButtonClick}
            required
          />
          <button type="button" onClick={handleSearchButtonClick}>
            검색
          </button>
        </>
      ),
    },
  ];

  return (
    <EntireContainer>
      <CardContainer>
        <span>(미리 보기)</span>
        <CardView card={card} />
      </CardContainer>
      <form onSubmit={handleSubmit} autocomplete="off">
        {labels.map((label) => (
          <InputWrapper key={label.id}>
            <label htmlFor={label.id}>{label.title}</label>
            <InputItems>{label.children}</InputItems>
          </InputWrapper>
        ))}
        {openPostcode && (
          <div>
            <DaumPostcode
              onComplete={handleSelectAddress}
              autoClose={true} // 값을 선택할 경우 자동 닫힘
            />
          </div>
        )}
        <SubmitButtonWrapper>
          <button type="submit" disabled={disabled}>
            초대장 만들기
          </button>
        </SubmitButtonWrapper>
      </form>
    </EntireContainer>
  );
}
