import React from "react";
import { Trash2 } from "lucide-react";

export default function AdminRequestCard({ request, onDelete }) {
  const {
    id,
    requesterUsername,
    targetUsername,
    offeredSkill,
    wantedSkill,
    status,
    type,
    createdAt,
  } = request;

  return (
    <div className="bg-white rounded-xl shadow-md hover:shadow-lg transition-shadow duration-300 p-4 space-y-3">
      {/* Header with usernames and delete button */}
      <div className="flex justify-between items-start gap-3">
        <div>
          <p className="text-sm text-gray-600 mb-1">
            <strong>{requesterUsername}</strong> ➡{" "}
            <strong>{targetUsername}</strong>
          </p>

          <div className="flex flex-wrap items-center gap-2 text-sm">
            <SkillBadge
              label="Offered"
              value={offeredSkill?.name || "—"}
              color="blue"
            />
            <SkillBadge
              label="Wanted"
              value={wantedSkill?.name || "—"}
              color="green"
            />
          </div>
        </div>

        <button
          onClick={() => onDelete(id)}
          className="text-red-500 hover:text-red-700"
          title="Delete Request"
        >
          <Trash2 className="w-5 h-5" />
        </button>
      </div>

      {/* Meta details */}
      <div className="text-xs text-gray-500 flex flex-wrap gap-3">
        <span className="capitalize">Status: {status}</span>
        <span className="capitalize">Type: {type}</span>
        <span>
          Created:{" "}
          {createdAt
            ? new Date(createdAt).toLocaleDateString("en-IN", {
                year: "numeric",
                month: "short",
                day: "numeric",
              })
            : "—"}
        </span>
      </div>
    </div>
  );
}

function SkillBadge({ label, value, color }) {
  const colorClasses = {
    blue: "bg-blue-100 text-blue-700",
    green: "bg-green-100 text-green-700",
    purple: "bg-purple-100 text-purple-700",
  };

  const baseClasses =
    "inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium";

  return (
    <span
      className={`${baseClasses} ${
        colorClasses[color] || "bg-gray-100 text-gray-700"
      }`}
    >
      {label}: <span className="ml-1 font-semibold">{value}</span>
    </span>
  );
}
