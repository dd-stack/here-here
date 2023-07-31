import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import DaumPostcode from "react-daum-postcode";

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

  const userInfo = useSelector((state) => state.user?.userInfo);
  const { email } = JSON.parse(userInfo);

  // 로그인이 되어 있지 않다면 로그인 화면으로
  useEffect(() => {
    if (!userInfo) {
      navigate("/login");
    }
  }, [userInfo, navigate]);

  const [card, setCard] = useState({
    email: email,
    title: "",
    startTime: "",
    endTime: "",
    background: "#fff",
    content: "",
    textLocation: "center",
    textColor: "#0C0A09",
    location: "",
  });
  const [disabled, setDisabled] = useState(false);
  const [openPostcode, setOpenPostcode] = useState(false);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setCard((previous) => ({ ...previous, [name]: value }));
  };

  const handleBoxChange = (key, value) => {
    setCard((previous) => ({ ...previous, [key]: value }));
  };

  const handleColorChange = (value) => {
    // 이미 이미지 url이 들어 있다면 해당 이미지 삭제 후 색으로 변경
    if (!card.background.startsWith("#")) {
      deleteImage(card.background);
    }
    setCard((previous) => ({ ...previous, background: value }));
  };

  const handleImageChange = (event) => {
    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    // 이미 이미지 url이 들어 있다면 해당 이미지 삭제 후 업로드 요청
    if (!card.background.startsWith("#")) {
      deleteImage(card.background);
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

  const handleClick = () => {
    setOpenPostcode((current) => !current);
  };

  const handleSelectAddress = (data) => {
    setCard((previous) => ({ ...previous, location: data.address }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setDisabled(true);

    Swal.fire({
      text: "초대장을 만드는 중입니다.",
      showConfirmButton: false,
      timer: 1200,
      timerProgressBar: true,
      padding: "20px 40px 40px",
    });

    setTimeout(() => {
      postCard(card).then((result) => {
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
          name="title"
          placeholder="캘린더 등록 시 보이는 문구입니다."
          maxLength="20"
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
          onChange={(event) => {
            handleInputChange(event);
            setCard((previous) => ({ ...previous, endTime: "" })); // 시작 날짜가 변경되면 종료 날짜를 초기화
          }}
          min={new Date().toISOString().slice(0, 16)} // 현재 시간 이후만 선택 가능
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
          disabled={!card.startTime} // 시작 날짜가 선택되지 않은 경우에는 비활성화
          min={card.startTime} // 시작 날짜 이후만 선택 가능
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
            onChange={handleInputChange}
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
          <button type="button" onClick={handleClick}>
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
      <form onSubmit={handleSubmit}>
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
