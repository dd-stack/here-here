import styled from "styled-components";

const CardContainer = styled.div`
  display: flex;
  align-items: ${(props) => {
    switch (props.location) {
      case "top":
        return "flex-start";
      case "center":
        return "center";
      case "bottom":
        return "flex-end";
      default:
        return "center";
    }
  }};
  justify-content: center;
  height: 330px;
  width: 330px;
  background: ${(props) => (props.color.startsWith("#") ? props.color : `url(${props.color})`)};
  background-size: cover;
  background-position: center;
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

const CardContent = styled.div`
  padding: 20px;
  text-align: center;
  font-size: 20px;
  color: ${(props) => props.color};
  white-space: pre-wrap;
`;

export default function CardView({ card }) {
  return (
    <CardContainer color={card.background} location={card.textLocation}>
      <CardContent color={card.textColor}>{card.content}</CardContent>
    </CardContainer>
  );
}
