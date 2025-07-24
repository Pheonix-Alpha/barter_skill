import { useEffect, useState } from "react";
import axios from "axios";

const ProfilePanel = () => {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      const token = localStorage.getItem("token");
      try {
        const res = await axios.get("http://localhost:8080/api/users/me/profile", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setProfile(res.data);
      } catch (err) {
        console.error("Failed to fetch profile", err);
      }
    };

    fetchProfile();
  }, []);

  if (!profile) return <div>Loading profile...</div>;

  return (
    <div className="p-4 bg-white shadow rounded-lg">
      <h2 className="text-xl font-semibold mb-2">👤 My Profile</h2>
      <p><strong>Username:</strong> {profile.username}</p>
      <p><strong>Email:</strong> {profile.email}</p>
      <p><strong>Friend:</strong> {profile.friend ? "Yes" : "No"}</p>
      <p><strong>Request Sent:</strong> {profile.requestSent ? "Yes" : "No"}</p>
      <p><strong>Request Received:</strong> {profile.requestReceived ? "Yes" : "No"}</p>
    </div>
  );
};

export default ProfilePanel;
