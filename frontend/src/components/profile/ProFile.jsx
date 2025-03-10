import React, { use, useEffect } from "react";
import { Row, Col, Card, Avatar, Descriptions, Button, Spin } from "antd";
import styled from "styled-components";
import useProfile from "../../hooks/useProFile"; // Điều chỉnh đường dẫn
import avatar from "../../assets/avatar_icon.png";
const StyledContainer = styled.div`
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
  display: flex;
  justify-content: center;
`;

const ProfileCard = styled(Card)`
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  background: #ffffff;
  border: 1px solid #cccccc;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    transform: translateY(-2px);
  }

  .ant-card-body {
    padding: 24px;
    text-align: center;
  }
`;

const InfoSection = styled(Descriptions)`
  margin-top: 20px;
  background: #ffffff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

  .ant-descriptions-item-label {
    font-weight: 500;
    color: #2e8b57;
  }

  .ant-descriptions-item-content {
    color: #333333;
  }
`;

const StyledButton = styled(Button)`
  padding: 0 24px;
  height: 40px;
  border-radius: 6px;
  font-weight: 500;
  background: #3cb371;
  border-color: #3cb371;
  color: white;
  margin-top: 20px;

  &:hover {
    background: #2e8b57 !important;
    border-color: #2e8b57 !important;
  }

  &:disabled {
    background: #a9a9a9;
    border-color: #a9a9a9;
    color: #ffffff;
  }
`;

const Profile = () => {
  const { profile, loading, error, fetchProfile } = useProfile();

  useEffect(() => {
    fetchProfile();
  }, [fetchProfile]);

  if (loading)
    return <Spin size="large" style={{ display: "block", margin: "auto" }} />;
  if (error)
    return (
      <div style={{ textAlign: "center", color: "#ff4500" }}>
        Error: {error}
      </div>
    );

  return (
    <StyledContainer>
      <Row gutter={[24, 24]} style={{ maxWidth: "1200px", width: "100%" }}>
        {/* Avatar và thông tin cơ bản */}
        <Col xs={24} md={8}>
          <ProfileCard>
            <Avatar size={200} src={avatar} style={{ margin: "0 auto 20px" }} />
            <h2 style={{ color: "#2e8b57", marginBottom: "10px" }}>
              {profile?.username || "Unknown"}
            </h2>
            <p style={{ color: "#666", marginBottom: "5px" }}>
              {profile?.roles[0]?.roleName || "Unknown"}
            </p>
            <p style={{ color: "#666" }}>{profile?.location || "Unknown"}</p>
            <StyledButton>Edit</StyledButton>
          </ProfileCard>
        </Col>

        {/* Thông tin chi tiết */}
        <Col xs={24} md={16}>
          <Card bordered={false}>
            <InfoSection
              title="Profile Details"
              layout="vertical"
              column={{ xxl: 1, xl: 1, lg: 1, md: 1, sm: 1, xs: 1 }}
              bordered
            >
              <Descriptions.Item label="Full Name">
                {profile?.employee?.fullName || "N/A"}
              </Descriptions.Item>
              <Descriptions.Item label="Email">
                {profile?.employee?.email || "N/A"}
              </Descriptions.Item>
              <Descriptions.Item label="Gender">
                {profile?.employee?.gender || "N/A"}
              </Descriptions.Item>
              <Descriptions.Item label="Date of Birth">
                {profile?.employee?.dateOfBirth || "N/A"}
              </Descriptions.Item>
            </InfoSection>
          </Card>
        </Col>
      </Row>
    </StyledContainer>
  );
};

export default Profile;
