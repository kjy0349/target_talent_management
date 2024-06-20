import { useState, KeyboardEvent } from "react";
import { Select, TextInput } from "flowbite-react";
import { HiX } from "react-icons/hi";

interface Result {
  executiveId: number;
  grade: string;
}

interface TagManagerProps {
  onUpdatePairs: (newPairs: Result[]) => void;
}

const TagManager = ({ onUpdatePairs }: TagManagerProps) => {
  const gradeOptions: string[] = ["A+", "A0", "B+", "B0", "C+", "C0", "D", "F"];
  const [pairs, setPairs] = useState<Result[]>([]);
  const [gradeInput, setGradeInput] = useState("A+");
  const [judgeInput, setJudgeInput] = useState("");

  const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && judgeInput.trim() !== "") {
      event.preventDefault();
      const executiveId = parseInt(judgeInput, 10); // Convert input to integer
      if (!Number.isNaN(executiveId)) {
        // Check if conversion was successful
        const newPair: Result = { executiveId, grade: gradeInput };
        if (!pairs.some((p) => p.executiveId === newPair.executiveId)) {
          const newPairs = [...pairs, newPair];
          setPairs(newPairs);
          onUpdatePairs(newPairs);
          setJudgeInput(""); // Reset input field
        }
      }
    }
  };

  const handleJudgeInputChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setJudgeInput(event.target.value);
  };

  const handleGradeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setGradeInput(event.target.value);
  };

  const removeItem = (index: number) => {
    const newPairs = pairs.filter((_, idx) => idx !== index);
    setPairs(newPairs);
    onUpdatePairs(newPairs);
  };

  return (
    <div className="my-5 rounded-xl border p-4">
      <div className="mb-2 flex flex-row gap-4">
        <Select
          value={gradeInput}
          onChange={handleGradeChange}
          className="input input-bordered w-1/2 font-bold"
        >
          {gradeOptions.map((gradeOption) => (
            <option key={gradeOption} value={gradeOption}>
              {gradeOption}
            </option>
          ))}
        </Select>
        <TextInput
          type="text"
          value={judgeInput}
          onChange={handleJudgeInputChange}
          onKeyDown={handleKeyDown}
          placeholder="직급4 ID" // Changed placeholder to reflect expected input
          className="input input-bordered w-full"
        />
      </div>
      <div className="flex flex-wrap gap-2">
        {pairs.map((pair, index) => (
          <div
            key={index}
            className="mb-2 mr-2 flex cursor-pointer items-center rounded border border-slate-300 bg-slate-50 p-2"
            onClick={() => removeItem(index)}
          >
            {`${pair.grade} / ${pair.executiveId}`}{" "}
            <HiX className="ml-2 inline-block text-red-500" />
          </div>
        ))}
      </div>
    </div>
  );
};

export default TagManager;
