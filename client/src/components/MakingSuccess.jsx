import { useState } from "react";

import styled from "styled-components";
import { FaLink } from "react-icons/fa";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: var(--background-color);
`;

const ShareUrlContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 80%;
  margin: 40px 0;
  padding: 5%;
  border: 2px solid var(--third-theme-color);
  border-radius: 10px;
  background-color: #fff;
  > input {
    width: 50%;
  }
`;

const CopyButton = styled.button`
  padding: 5px 10px;
  background-color: var(--third-theme-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
`;

const ShareSnsContainer = styled.div`
  width: 80%;
  height: 100px;
  border: 1px solid var(--gray-color);
`;

export default function MakingSuccess({ cardId }) {
  const url = `http://localhost:3000/card/${cardId}`;
  const [isCopied, setIsCopied] = useState(false);

  const copyToClipboard = () => {
    navigator.clipboard.writeText(url);
    setIsCopied(true);
  };

  return (
    <EntireContainer>
      <span>초대장이 성공적으로 만들어졌습니다!</span>
      <ShareUrlContainer>
        <label htmlFor="url">
          <FaLink />
        </label>
        <input id="url" type="url" value={url} readOnly />
        <CopyButton onClick={copyToClipboard}>
          {isCopied ? "복사 완료!" : "URL 복사하기"}
        </CopyButton>
      </ShareUrlContainer>
      <ShareSnsContainer>카카오 인스타 트위터</ShareSnsContainer>
    </EntireContainer>
  );
}
