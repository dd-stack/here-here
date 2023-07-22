import { useState, useEffect } from "react";
import { FacebookShareButton, FacebookIcon, TwitterIcon, TwitterShareButton } from "react-share";

import styled from "styled-components";
import { FaLink } from "react-icons/fa";

const SnsShareContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 330px;
  margin: 100px 0 50px 0;
  padding: 30px 0;
  border: 2px solid var(--third-theme-color);
  border-radius: 10px;
  background-color: #fff;
  > span {
    margin: 10px 0 30px 0;
  }
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 20px;
`;

const LinkShareContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  border-radius: 25px;
  background-color: #ccc;
  cursor: pointer;
`;

const KakaoButtonContainer = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 25px;
  overflow: hidden;
`;

const NotificationText = styled.span`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 10px;
  border-radius: 10px;
  background-color: var(--third-theme-color);
  z-index: 2;
`;

export default function SnsShare({ cardId }) {
  // 실제 도메인 주소로 변경해야 함
  const url = `http://localhost:3000/card/${cardId}`;
  const [isCopied, setIsCopied] = useState(false);

  useEffect(() => {
    // 최신 Kakao SDK 로드
    const kakaoScript = document.createElement("script");
    kakaoScript.src = "https://t1.kakaocdn.net/kakao_js_sdk/2.3.0/kakao.min.js";
    kakaoScript.integrity =
      "sha384-70k0rrouSYPWJt7q9rSTKpiTfX6USlMYjZUtr1Du+9o4cGvhPAWxngdtVZDdErlh";
    kakaoScript.crossOrigin = "anonymous";
    kakaoScript.async = true;
    document.body.appendChild(kakaoScript);

    kakaoScript.onload = () => {
      const Kakao = window.Kakao;
      // 초기화되지 않았다면 초기화
      if (!Kakao.isInitialized()) {
        Kakao.init(process.env.REACT_APP_JAVASCRIPT_KEY);
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
  }, [url]);

  const copyToClipboard = () => {
    navigator.clipboard.writeText(url);
    setIsCopied(true);

    setTimeout(() => {
      setIsCopied(false);
    }, 1500);
  };

  return (
    <SnsShareContainer>
      <span>초대장 공유하기</span>
      <ButtonsWrapper>
        <LinkShareContainer onClick={copyToClipboard}>
          <FaLink size={24} />
        </LinkShareContainer>
        {isCopied && <NotificationText>링크가 복사되었습니다.</NotificationText>}
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
    </SnsShareContainer>
  );
}
