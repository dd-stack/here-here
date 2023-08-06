import { Link } from "react-router-dom";
import { useSelector } from "react-redux";

import styled from "styled-components";
import { BsBoxArrowUpRight } from "react-icons/bs";
import makingCardSample from "../img/making-card-sample.png";
import shareMessageSample from "../img/share-message-sample.png";
import calendarSample from "../img/calendar-sample.png";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 80px;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const GreetingText = styled.div`
  margin-top: 80px;
  > p {
    font-size: 24px;
    font-family: "KyoboHand";
  }
  .highlight {
    font-family: "KyoboHand";
    box-shadow: inset 0 -10px 0 #d9fcdb;
  }
`;

const ExplainWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  > img {
    width: 300px;
  }
  > p {
    font-size: 24px;
    font-family: "KyoboHand";
  }
`;

const BlinkingText = styled.span`
  margin-top: 60px;
  animation: blink 2s infinite;
  @keyframes blink {
    50% {
      opacity: 0;
    }
  }
`;

const LoginButton = styled(Link)`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 150px;
  height: 50px;
  margin-bottom: 80px;
  border: 2px solid var(--second-theme-color);
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  background-color: var(--third-theme-color);
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

const MakingButton = styled(LoginButton)``;

const MoreInfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  @media (max-width: 480px) {
    gap: 30px;
  }
`;

const MoreInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 15px;
  > a {
    display: flex;
    gap: 5px;
    color: var(--link-color);
    > svg > path {
      color: var(--link-color);
    }
  }
  .emailLink {
    text-decoration: underline;
  }
  @media (max-width: 480px) {
    display: flex;
    flex-direction: column;
  }
`;

export default function Info() {
  const isLogin = useSelector((state) => state.user?.userInfo);

  return (
    <EntireContainer>
      <GreetingText>
        <p>
          Hi there 👋 <br />
          <br />
          <span className="highlight">"여기 여기 붙어라"</span>는 <br />
          가볍게 주고 받을 수 있는 <br />
          캐주얼 초대장 서비스입니다.
        </p>
      </GreetingText>
      <ExplainWrapper>
        <img src={makingCardSample} alt="making card sample" />
        <p>간단하게 초대장을 만들고,</p>
      </ExplainWrapper>
      <ExplainWrapper>
        <img src={shareMessageSample} alt="share message sample" />
        <p>친구들에게 공유해 보세요!</p>
      </ExplainWrapper>
      <ExplainWrapper>
        <img src={calendarSample} alt="calendar sample" />
        <p>
          초대장 수락하기 기능을 통해
          <br /> 톡캘린더에 일정을 등록할 수도 있어요.
        </p>
      </ExplainWrapper>
      <BlinkingText>🔻🔻🔻</BlinkingText>
      {isLogin ? (
        <MakingButton to="/making">초대장 만들러 가기</MakingButton>
      ) : (
        <LoginButton to="/login">로그인하러 가기</LoginButton>
      )}
      <MoreInfoWrapper>
        <MoreInfo>
          <p>💡 이 프로젝트가 더 궁금하시다면?</p>
          <a href="https://github.com/dd-stack/here-here" target="_blank" rel="noopener noreferrer">
            go to github <BsBoxArrowUpRight />
          </a>
        </MoreInfo>
        <MoreInfo>
          <p>💡 오류 제보, 피드백, 문의 사항은?</p>
          <a className="emailLink" href="mailto:herehereproject@gmail.com">
            herehereproject@gmail.com
          </a>
        </MoreInfo>
      </MoreInfoWrapper>
    </EntireContainer>
  );
}
