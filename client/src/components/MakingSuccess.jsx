import { useState, useEffect } from "react";
import { FacebookShareButton, FacebookIcon, TwitterIcon, TwitterShareButton } from "react-share";

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
  margin: 10% 0;
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
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 25px;
  width: 80%;
  height: 150px;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 20px;
`;

const KakaoButtonContainer = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 25px;
  overflow: hidden;
`;

export default function MakingSuccess({ cardId }) {
  // 실제 도메인 주소로 변경해야 함
  const url = `http://localhost:3000/card/${cardId}`;
  const [isCopied, setIsCopied] = useState(false);

  useEffect(() => {
    // 최신 Kakao SDK 로드
    const kakaoScript = document.createElement("script");
    kakaoScript.src = "https://t1.kakaocdn.net/kakao_js_sdk/2.3.0/kakao.min.js";
    kakaoScript.async = true;
    kakaoScript.integrity =
      "sha384-70k0rrouSYPWJt7q9rSTKpiTfX6USlMYjZUtr1Du+9o4cGvhPAWxngdtVZDdErlh";
    kakaoScript.crossOrigin = "anonymous";
    document.body.appendChild(kakaoScript);

    kakaoScript.onload = () => {
      if (window.Kakao) {
        const Kakao = window.Kakao;
        // 초기화되지 않았다면 초기화
        if (!Kakao.isInitialized()) {
          Kakao.init(process.env.REACT_APP_JAVASCRIPT_KEY);
        }
      }
    };
  }, []);

  useEffect(() => {
    // 카카오 링크 버튼 생성
    if (window.Kakao && document.querySelector("#kakaotalk-sharing-btn")) {
      const Kakao = window.Kakao;
      Kakao.Share.createDefaultButton({
        container: "#kakaotalk-sharing-btn",
        objectType: "feed",
        content: {
          title: "띵동! 초대장이 도착했어요 ~🎵",
          description: "자세히 보기를 눌러 확인해보세요!",
          imageUrl:
            "https://github.com/dd-stack/here-here/blob/fe/client/src/img/letter.png?raw=true",
          link: {
            webUrl: url,
          },
        },
        buttons: [
          {
            title: "자세히 보기",
            link: {
              webUrl: url,
            },
          },
        ],
      });
    }
  }, []);

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
      <ShareSnsContainer>
        <span>SNS로 공유하기</span>
        <ButtonsWrapper>
          <KakaoButtonContainer>
            <a id="kakaotalk-sharing-btn" href="javascript:;">
              <img
                src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png"
                alt="카카오톡 공유 보내기 버튼"
              />
            </a>
          </KakaoButtonContainer>
          <FacebookShareButton url={url}>
            <FacebookIcon size={50} round={true}></FacebookIcon>
          </FacebookShareButton>
          <TwitterShareButton url={url}>
            <TwitterIcon size={50} round={true}></TwitterIcon>
          </TwitterShareButton>
        </ButtonsWrapper>
      </ShareSnsContainer>
    </EntireContainer>
  );
}
