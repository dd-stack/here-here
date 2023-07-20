import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import DaumPostcode from "react-daum-postcode";

import styled from "styled-components";
import { FcCameraIdentification } from "react-icons/fc";

import { postImage } from "../api/image";
import { postCard } from "../api/card";
import CardCardView from "../components/CardView";
import MakingSuccess from "../components/MakingSuccess";

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

  // // 로그인이 되어 있지 않다면 로그인 화면으로
  // useEffect(() => {
  //   if (!isLogin) {
  //     navigate("/login");
  //   }
  // }, [isLogin, navigate]);

  const [card, setCard] = useState({
    title: "",
    startTime: "",
    endTime: "",
    background: "#fff",
    content: "",
    textLocation: "center",
    textColor: "#0C0A09",
    location: "",
  });
  console.log(card);
  const [disabled, setDisabled] = useState(false);
  const [openPostcode, setOpenPostcode] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState(false);
  const [cardId, setCardId] = useState(null);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setCard((previous) => ({ ...previous, [name]: value }));
  };

  const handleBoxChange = (key, value) => {
    if (value !== card.key) {
      setCard((previous) => ({ ...previous, [key]: value }));
    }
  };

  const handleClick = () => {
    setOpenPostcode((current) => !current);
  };

  const handleSelectAddress = (data) => {
    setCard((previous) => ({ ...previous, location: data.address }));
  };

  const handleImageUpload = (event) => {
    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    // 만약에 이미 url이 들어 있다면 해당 이미지 삭제 후 업로드 요청
    postImage(formData).then((response) => {
      if (response !== "fail") {
        setCard((previous) => ({ ...previous, background: response.image }));
      }
      if (response === "fail") {
        alert("이미지 등록에 실패했습니다.");
      }
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setDisabled(true);
    postCard(card).then((result) => {
      if (result !== "fail") {
        // 데이터 오는 형식 임의로
        setCardId(result.data.id);
        setSubmitSuccess(true);
        setDisabled(false);
      }
      if (result === "fail") {
        alert("초대장 만들기에 실패했습니다.");
        setDisabled(false);
      }
    });
  };

  const labels = [
    {
      id: "title",
      title: "초대장 제목 :",
      children: (
        <input
          id="title"
          type="text"
          name="title"
          placeholder="공유 시 보이는 문구입니다."
          maxLength="50"
          value={card.title}
          onChange={handleInputChange}
          required
        />
      ),
    },
    {
      id: "startTime",
      title: "시작 날짜 :",
      children: (
        <input
          id="startTime"
          type="datetime-local"
          name="startTime"
          value={card.startTime}
          onChange={handleInputChange}
          required
        />
      ),
    },
    {
      id: "endTime",
      title: "종료 날짜 :",
      children: (
        <input
          id="endTime"
          type="datetime-local"
          name="endTime"
          value={card.endTime}
          onChange={handleInputChange}
          required
        />
      ),
    },
    {
      id: "background",
      title: "배경 :",
      children: (
        <>
          <ColorBox color="#F1CBD2" onClick={() => handleBoxChange("background", "#F1CBD2")} />
          <ColorBox color="#F2C9B7" onClick={() => handleBoxChange("background", "#F2C9B7")} />
          <ColorBox color="#EFDA92" onClick={() => handleBoxChange("background", "#EFDA92")} />
          <ColorBox color="#B5D9CD" onClick={() => handleBoxChange("background", "#B5D9CD")} />
          <ColorBox color="#C3D0D7" onClick={() => handleBoxChange("background", "#C3D0D7")} />
          <ColorBox color="#B2B2CD" onClick={() => handleBoxChange("background", "#B2B2CD")} />
          <ColorInput
            id="background"
            type="color"
            name="background"
            value={card.background}
            onChange={handleInputChange}
          />
          <ImageInput>
            <label htmlFor="imageInput">
              <FcCameraIdentification size="50px" />
            </label>
            <input id="imageInput" type="file" accept="image/*" onChange={handleImageUpload} />
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
          name="content"
          placeholder="일정, 준비물, 초대 문구 등 상세 내용을 적어주세요. (최대 150자)"
          maxLength="150"
          value={card.content}
          onChange={handleInputChange}
        />
      ),
    },
    {
      id: "textLocation",
      title: "텍스트 위치 :",
      children: (
        <>
          <TextLocationBox onClick={() => handleBoxChange("textLocation", "top")}>
            위
          </TextLocationBox>
          <TextLocationBox onClick={() => handleBoxChange("textLocation", "center")}>
            중간
          </TextLocationBox>
          <TextLocationBox onClick={() => handleBoxChange("textLocation", "bottom")}>
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
          <ColorBox color="#fdfcfa" onClick={() => handleBoxChange("textColor", "#fdfcfa")} />
          <ColorBox color="#9E9E9E" onClick={() => handleBoxChange("textColor", "#9E9E9E")} />
          <ColorBox color="#0C0A09" onClick={() => handleBoxChange("textColor", "#0C0A09")} />
          <ColorInput
            id="textColor"
            type="color"
            name="textColor"
            value={card.textColor}
            onChange={handleInputChange}
          />
        </>
      ),
    },
    {
      id: "location",
      title: "장소(선택) :",
      children: (
        <>
          <input
            id="location"
            type="text"
            name="location"
            placeholder="주소를 검색해 주세요."
            value={card.location}
            onClick={handleClick}
            readOnly
          />
          <button onClick={handleClick}>검색</button>
        </>
      ),
    },
  ];

  return (
    <>
      {submitSuccess ? (
        <MakingSuccess cardId={cardId} />
      ) : (
        <EntireContainer>
          <CardContainer>
            <span>(미리 보기)</span>
            <CardCardView card={card} />
          </CardContainer>
          <form onSubmit={handleSubmit}>
            {labels.map((label) => (
              <InputWrapper key={label.id}>
                <label htmlFor={label.id}>{label.title}</label>
                <InputItems>{label.children}</InputItems>
              </InputWrapper>
            ))}
            {openPostcode && (
              <DaumPostcode
                onComplete={handleSelectAddress}
                autoClose={true} // 값을 선택할 경우 자동 닫힘
              />
            )}
            <SubmitButtonWrapper>
              <button type="submit" disabled={disabled}>
                초대장 만들기
              </button>
            </SubmitButtonWrapper>
          </form>
        </EntireContainer>
      )}
    </>
  );
}
