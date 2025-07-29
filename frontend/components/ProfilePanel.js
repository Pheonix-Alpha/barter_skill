import { useEffect, useState } from "react";
import axios from "axios";
import { useSearchParams, useRouter } from "next/navigation";

const ProfilePanel = () => {
  const [profile, setProfile] = useState(null);
  const searchParams = useSearchParams();
  const refreshKey = searchParams.get("updated");
  const router = useRouter();

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
  }, [refreshKey]);

  if (!profile) return <div>Loading profile...</div>;

  const getInitial = (name) => name?.charAt(0)?.toUpperCase();

  return (
    <div className="p-6 bg-white shadow rounded-lg max-w-3xl mx-auto mt-6">
      {/* Top Section with Title and Edit Button */}
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-3xl font-bold">👤 Profile</h2>
        <button
          onClick={() => router.push("/profile/edit")}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Edit Profile
        </button>
      </div>

      {/* Profile Picture & Info */}
      <div className="flex items-center gap-6">
        {profile.profilePicture ? (
          <img
            src={profile.profilePicture}
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
            <p className="font-medium">Offering:</p>
            <ul className="list-disc list-inside text-gray-700">
              {profile.offeringSkills?.length ? (
                profile.offeringSkills.map((skill, i) => <li key={i}>{skill}</li>)
              ) : (
                <li>None</li>
              )}
            </ul>
          </div>
          <div>
            <p className="font-medium">Wanting:</p>
            <ul className="list-disc list-inside text-gray-700">
              {profile.wantingSkills?.length ? (
                profile.wantingSkills.map((skill, i) => <li key={i}>{skill}</li>)
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
