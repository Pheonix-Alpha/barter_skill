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

  if (!profile) return <div className="p-4">Loading profile...</div>;

  const getInitial = (name) => name?.charAt(0)?.toUpperCase();

  return (
    <div className="p-4 sm:p-6 bg-white shadow rounded-lg w-full max-w-4xl mx-auto">
      {/* Top Section */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
        <h2 className="text-2xl font-bold text-gray-800">👤 Profile</h2>
        <button
          onClick={() => router.push("/profile/edit")}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm sm:text-base"
        >
          Edit Profile
        </button>
      </div>

      {/* Profile Image + Info */}
      <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6">
        {profile.profilePicture ? (
          <img
            src={profile.profilePicture}
            alt="Profile"
            className="w-24 h-24 rounded-full object-cover"
          />
        ) : (
          <div className="w-24 h-24 rounded-full bg-blue-500 text-white flex items-center justify-center text-4xl font-bold">
            {getInitial(profile.username)}
          </div>
        )}
        <div className="text-center sm:text-left">
          <h3 className="text-xl font-semibold">{profile.username}</h3>
          <p className="text-gray-600">{profile.bio || "No bio provided."}</p>
        </div>
      </div>

      {/* Skills */}
      <div className="mt-6">
        <h4 className="text-lg font-semibold mb-2">🛠️ Skills</h4>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
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

      {/* Reviews Placeholder */}
      <div className="mt-6">
        <h4 className="text-lg font-semibold mb-2">⭐ Reviews</h4>
        <p className="text-gray-500 text-sm">No reviews yet. Coming soon!</p>
      </div>
    </div>
  );
};

export default ProfilePanel;
