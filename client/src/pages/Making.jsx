import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import styled from "styled-components";
import { FcCameraIdentification } from "react-icons/fc";

import { postImage } from "../api/image";

import Card from "../components/Card";

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

const TextColorBox = styled(ColorBox)`
  border: 1px solid var(--gray-color);
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
    background: "#fff",
    content: "",
    textLocation: "center",
    textColor: "#0C0A09",
  });
  console.log(card);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setCard((previous) => ({ ...previous, [name]: value }));
  };

  const handleColorChange = (color) => {
    if (color !== card.background) {
      setCard((previous) => ({ ...previous, background: color }));
    }
  };

  const handleTextLocationChange = (location) => {
    if (location !== card.textLocation) {
      setCard((previous) => ({ ...previous, textLocation: location }));
    }
  };

  const handleTextColorChange = (color) => {
    if (color !== card.textColor) {
      setCard((previous) => ({ ...previous, textColor: color }));
    }
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
          value={card.title}
          onChange={handleChange}
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
            name="background"
            value={card.background}
            onChange={handleChange}
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
          onChange={handleChange}
          required
        />
      ),
    },
    {
      id: "textLocation",
      title: "텍스트 위치 :",
      children: (
        <>
          <TextLocationBox onClick={() => handleTextLocationChange("top")}>위</TextLocationBox>
          <TextLocationBox onClick={() => handleTextLocationChange("center")}>중간</TextLocationBox>
          <TextLocationBox onClick={() => handleTextLocationChange("bottom")}>아래</TextLocationBox>
        </>
      ),
    },
    {
      id: "textColor",
      title: "텍스트 색 :",
      children: (
        <>
          <TextColorBox color="#fdfcfa" onClick={() => handleTextColorChange("#fdfcfa")} />
          <TextColorBox color="#9E9E9E" onClick={() => handleTextColorChange("#9E9E9E")} />
          <TextColorBox color="#0C0A09" onClick={() => handleTextColorChange("#0C0A09")} />
          <ColorInput
            id="textColor"
            type="color"
            name="textColor"
            value={card.textColor}
            onChange={handleChange}
          />
        </>
      ),
    },
  ];

  return (
    <EntireContainer>
      <CardContainer>
        <span>(미리 보기)</span>
        <Card card={card} />
      </CardContainer>
      {labels.map((label) => (
        <InputWrapper key={label.id}>
          <label htmlFor={label.id}>{label.title}</label>
          <InputItems>{label.children}</InputItems>
        </InputWrapper>
      ))}
    </EntireContainer>
  );
}
