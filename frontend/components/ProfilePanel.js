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

  const getInitial = (name) => name?.charAt(0)?.toUpperCase();

  return (
    <div className="p-6 bg-white shadow rounded-lg max-w-3xl mx-auto mt-6">
      <h2 className="text-3xl font-bold mb-6">👤 Profile</h2>

      {/* Top Section with Profile Pic and Info */}
      <div className="flex items-center gap-6">
        {profile.imageUrl ? (
          <img
            src={profile.imageUrl}
            alt="Profile"
            className="w-20 h-20 rounded-full object-cover"
          />
        ) : (
          <div className="w-20 h-20 rounded-full bg-blue-500 text-white flex items-center justify-center text-3xl font-bold">
            {getInitial(profile.username)}
          </div>
        )}
        <div>
          <h3 className="text-2xl font-semibold">{profile.username}</h3>
          <p className="text-gray-600">{profile.bio || "No bio provided."}</p>
        </div>
      </div>

      {/* Skills Section */}
      <div className="mt-6">
        <h4 className="text-lg font-semibold mb-2">🛠️ Skills</h4>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <p className="font-medium">Offered:</p>
            <ul className="list-disc list-inside text-gray-700">
              {profile.offeredSkills?.length ? (
                profile.offeredSkills.map((skill, i) => <li key={i}>{skill}</li>)
              ) : (
                <li>None</li>
              )}
            </ul>
          </div>
          <div>
            <p className="font-medium">Wanted:</p>
            <ul className="list-disc list-inside text-gray-700">
              {profile.wantedSkills?.length ? (
                profile.wantedSkills.map((skill, i) => <li key={i}>{skill}</li>)
              ) : (
                <li>None</li>
              )}
            </ul>
          </div>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="mt-6">
        <h4 className="text-lg font-semibold mb-2">⭐ Reviews</h4>
        <p className="text-gray-500">No reviews yet. Coming soon!</p>
      </div>
    </div>
  );
};

export default ProfilePanel;
