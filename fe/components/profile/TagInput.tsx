import { useState, KeyboardEvent } from "react";
import { TextInput } from "flowbite-react";

interface TagManagerProps {
  tagProp: string[];
  onUpdateKeywords: (newKeywords: string[]) => void;
}

const TagManager = ({ tagProp, onUpdateKeywords }: TagManagerProps) => {
  const [tags, setTags] = useState<string[]>(tagProp);
  const [input, setInput] = useState("");

  const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && event.currentTarget.value.trim() !== "") {
      event.preventDefault();
      const newTag = event.currentTarget.value.trim();
      if (!tags.includes(newTag)) {
        const newTags = [...tags, newTag];
        setTags(newTags);
        setInput("");
        onUpdateKeywords(newTags);
      }
      event.currentTarget.value = "";
    }
  };

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInput(event.target.value);
  };

  const removeTag = (tag: string) => {
    const updatedTags = tags.filter((prevTag) => tag !== prevTag);
    setTags(updatedTags);
    onUpdateKeywords(updatedTags);
  };

  return (
    <div>
      <TextInput
        type="text"
        value={input}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        placeholder="키워드 입력 후 Enter 등록"
        className="input input-bordered mb-4 w-full"
      />
      <div className="flex flex-wrap gap-2">
        {tags &&
          tags.map((tag, index) => (
            <div
              key={index}
              className="m-2 mt-0 cursor-pointer rounded-md border p-2"
              onClick={() => removeTag(tag)}
            >
              {tag} ×
            </div>
          ))}
      </div>
    </div>
  );
};

export default TagManager;
