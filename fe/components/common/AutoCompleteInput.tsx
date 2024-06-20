import { useEffect, useRef, useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { TextInput } from "flowbite-react";
import { HiArrowDown } from "react-icons/hi";

import {
  AutoCompleteInputProps,
  AutoCompleteResponse,
} from "@/types/common/AutoComplete";
import { HiArrowDownCircle, HiArrowDownRight } from "react-icons/hi2";

export const AutoCompleteInput = (props: AutoCompleteInputProps) => {
  const localAxios = useLocalAxios();
  const [items, setItems] = useState<AutoCompleteResponse[]>([]);
  const [selectedItem, setSelectedItem] = useState<AutoCompleteResponse>();
  const [hoveredIndex, setHoveredIndex] = useState<number>(-1);
  const [query, setQuery] = useState<string>("");
  const [showDropdown, setShowDropdown] = useState<boolean>(false);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    localAxios
      .get(props.isKeyword ? "/keyword/type" : "/keyword", {
        params: props.isKeyword
          ? { keywordType: props.identifier, query }
          : { type: props.identifier, query },
      })
      .then((response) => {
        setItems(response.data);
      })
      .catch((err) => {
        console.error(err);
      });
  }, [props.identifier, query]);

  useEffect(() => {
    if (props.value) {
      setSelectedItem(props.value);
      setQuery(props.value.data);
    }
  }, [props.value]);

  useEffect(() => {
    if (hoveredIndex < 0 || items.length <= hoveredIndex) {
      setHoveredIndex(0);
    }
  }, [items, hoveredIndex]);

  const handleKeydown = (e: React.KeyboardEvent) => {
    switch (e.key) {
      case "Escape":
        inputRef.current?.blur();
        break;
      case "ArrowUp":
        setHoveredIndex(hoveredIndex - 1);
        break;
      case "ArrowDown":
        setHoveredIndex(hoveredIndex + 1);
        break;
      case "Enter":
        handleDropdownClick(hoveredIndex);
        break;
    }
  };

  const handleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };

  const handleFocus = (e: React.FocusEvent) => {
    setShowDropdown(true);
    setHoveredIndex(-1);
  };

  const handleBlur = (e: React.FocusEvent) => {
    if (!selectedItem) {
      setQuery("");
    }
    setTimeout(() => {
      setShowDropdown(false);
    }, 200);
  };

  const handleDropdownEnter = (hoveredIndex: number) => {
    setHoveredIndex(hoveredIndex);
  };

  const handleDropdownClick = (index: number) => {
    setSelectedItem(items[index]);
    inputRef.current?.blur();
    setQuery(items[index].data);

    if (props.onChange) props.onChange(items[index]);
  };

  return (
    <div className={"relative " + props.className}>
      <TextInput
        type="text"
        className="relative w-full"
        ref={inputRef}
        onKeyDown={handleKeydown}
        onInput={handleInput}
        onFocus={handleFocus}
        onBlur={handleBlur}
        value={query}
        rightIcon={HiArrowDownCircle}
        required={props.required}
        placeholder={props.placeholder ? props.placeholder : "(검색)"}
        disabled={props.disabled}
      />
      {showDropdown && (
        <ul className="absolute z-10 mt-1 w-full rounded-sm border-2 border-gray-600">
          {items.map((item, index) => (
            <button
              className={
                "block w-full cursor-pointer p-2 text-left " +
                (hoveredIndex == index ? "bg-gray-100" : "bg-white")
              }
              key={item.id}
              onMouseEnter={() => {
                handleDropdownEnter(index);
              }}
              onClick={(e) => {
                e.preventDefault();
                handleDropdownClick(index);
              }}
            >
              {item.data}
            </button>
          ))}
        </ul>
      )}
    </div>
  );
};
