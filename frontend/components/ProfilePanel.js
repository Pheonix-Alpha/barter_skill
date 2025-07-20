const ProfilePanel = () => {
  return (
    <div className="w-72 p-4 border-l bg-gray-50">
      <h2 className="text-lg font-semibold mb-4">Your Profile</h2>
      <img
        src="https://via.placeholder.com/100"
        alt="profile"
        className="rounded-full w-24 h-24 mx-auto mb-2"
      />
      <p className="text-center text-gray-700">Welcome, Username</p>

      <div className="mt-4 bg-white rounded-lg shadow p-3">
        <h3 className="text-sm font-medium mb-2">Recent Reviews</h3>
        <p className="text-xs text-gray-600">You completed 5 tasks this week</p>
      </div>

      <div className="mt-4 bg-white rounded-lg shadow p-3">
        <h3 className="text-sm font-medium mb-2">Your Skills</h3>
        <p className="text-xs text-gray-600">John Doe - Web Dev Expert</p>
      </div>
    </div>
  );
};

export default ProfilePanel;
