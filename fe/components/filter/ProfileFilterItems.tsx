'use-client'

import { HiPlus, HiX } from "react-icons/hi";
import { useState, useEffect } from "react";
import { AutoCompleteInput } from "../common/AutoCompleteInput";
import { useLocalAxios } from "@/app/api/axios";
import useFilterStore from "@/stores/filter";
import { useAuthStore } from "@/stores/auth";

interface props {
  seq: number,
  label: string,
  value: string,
  list: string[],
  addAble: boolean,
  identifier: string,
  isOption: boolean,
  onSelectionChange: (selected: string[]) => void;
}

const translateEdu = (edu: string) => {
  switch (edu) {
    case "BACHELOR":
      return "학사";
    case "MASTER":
      return "석사";
    case "pPollSize":
      return "박사";
  }
};

interface Item {
  data: string,
  id: number
}

export default function FilterItems({ props }: { props: props }) {
  const store = useFilterStore();
  const [showAddInput, setShowAddInput] = useState(false);
  const [list, setList] = useState<string[]>(props.list);
  const [selectedItems, setSelectedItems] = useState<string[]>([]);
  const localAxios = useLocalAxios();
  const authStore = useAuthStore();
  const dept = authStore.departmentName;

  useEffect(() => {
    const getList = async () => {
      if (props.identifier) {
        let response = null;
        if (props.isOption) {
          response = await localAxios.get("/keyword", {
            params: {
              type: props.identifier,
              query: "",
            }
          });
        } else {
          response = await localAxios.get("/keyword/type", {
            params: {
              keywordType: props.identifier
            }
          });
        }
        const newList: string[] = [];
        const responseArr: Item[] = response.data;
        if (props.identifier === "DEPARTMENT") {
          responseArr.forEach(item => {
            if (item.data !== dept) {
              newList.push(item.data);
            }
          });
        }
        else {
          responseArr.forEach(item => {
            newList.push(item.data);
          });
        }
        // console.log(props)
        if (store.filterResults[props.value]) {
          store.filterResults[props.value].map((item) => {
            if (!newList.includes(item)) newList.push(item);
          })
        }

        setList(newList);
      }
    };
    getList();
  }, [props.identifier]);

  const handleAddList = (newItem: string) => {
    setList([...list, newItem]);
    const newSelectedItems = [...selectedItems, newItem]
    setSelectedItems(newSelectedItems);
    props.onSelectionChange(newSelectedItems);
  }

  const handleCheckboxChange = (item: string, isChecked: boolean) => {
    const newSelectedItems = isChecked
      ? [...selectedItems, item]
      : selectedItems.filter(x => x !== item);
    setSelectedItems(newSelectedItems);
    props.onSelectionChange(newSelectedItems);
  };

  // const findIndex = () => {
  //   const filterResults = store.filterResults;
  //   return filterResults.(props.value).includes()
  // }

  return (
    <div className="my-4 border-b border-slate-200 p-4">
      <label className="text-lg font-bold">{props.label}</label>
      <div className="mt-4 flex w-full flex-row flex-wrap items-center">
        {list.map((item, index) => (
          <div key={item}
            className="mx-2 whitespace-nowrap"
          >
            <input
              id={`${props.seq * 1000 + index}`}
              type="checkbox"
              value={item}
              checked={store.filterResults[props?.value]?.includes(item) ?? false}
              onChange={e => handleCheckboxChange(item, e.target.checked)}
              className="size-4 rounded-full border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
            />
            <label htmlFor={`${props.seq * 1000 + index}`} className="ml-2 font-medium text-gray-900 dark:text-gray-300">
              {props.value === "degrees" ? translateEdu(item) : item}
            </label>
          </div>
        ))}
        {props.addAble && showAddInput && (
          <div className="flex flex-row gap-2">
            <AutoCompleteInput
              identifier={props.identifier}
              isKeyword={!props.isOption}
              onChange={(item) => {
                if (item && !list.includes(item.data)) {
                  handleAddList(item.data)
                  setShowAddInput(false);
                }
              }}
            />
            <button onClick={() => setShowAddInput(false)}>
              <HiX className="size-4" />
            </button>
          </div>
        )}
        {props.addAble && !showAddInput && (
          <button onClick={() => setShowAddInput(true)} className="w-24 p-2 text-primary">
            <HiPlus className="inline-block" />
            <span className="ml-2">추가</span>
          </button>
        )}
      </div>
    </div>
  );
}
