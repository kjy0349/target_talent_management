import CustomAddButton from "@/components/common/CustomAddButton";
import {Button, Card, Checkbox, Textarea} from "flowbite-react";
import {Memo} from "@/types/talent/ProfileDetailResponse";
import {useCallback, useState} from "react";
import {useLocalAxios} from "@/app/api/axios";
import {useAuthStore} from "@/stores/auth";
import {HiXMark} from "react-icons/hi2";

interface MemoSectionProps {
	data: Memo[];
	onCreate: (content: string, isPrivate: boolean) => void;
	onDelete: (id: number) => void;
}

const MemoSection = (props: MemoSectionProps) => {
	const localAxios = useLocalAxios();
	const authStore = useAuthStore();
	const [ writing, setWriting ] = useState<boolean>(false);
	const [ writingContent, setWritingContent ] = useState<string>("");
	const [ isPrivate, setIsPrivate ] = useState<boolean>(false);

	const startWriting = useCallback(() => {
		setWriting(true);
	}, []);

	const createMemo = () => {
		if (writingContent.length > 0) {
			props.onCreate(writingContent, isPrivate);
			setWritingContent("");
			setIsPrivate(false);
			setWriting(false);
		}
	};

	return (
		<Card className="bg-gray-50">
			<div className="flex flex-col justify-between">
				<div className="w-full flex justify-between items-center border-b-2 pb-2 mb-2">
					<p className="text-lg font-bold">메모</p>
					<CustomAddButton size="sm" onClick={startWriting} />
				</div>
				{ writing &&
					<div className="flex flex-col gap-2 border-b-2 pb-2 mb-2">
						<Textarea
							placeholder="(내용을 작성해주세요.)"
							className="resize-none bg-white"
							rows={3}
							value={writingContent}
							onChange={ (e: React.ChangeEvent<HTMLTextAreaElement>) => { setWritingContent(e.target.value); } }
						/>
						<div className="flex justify-between px-0.5">
							<label className="flex items-center gap-2">
								<Checkbox className="size-4" onChange={ (e: React.ChangeEvent<HTMLInputElement>) => { setIsPrivate(e.target.checked); } } />
								<span className="text-sm font-bold">나만 보기</span>
							</label>
							<div className="flex gap-2">
								<Button color="blue" outline onClick={createMemo}>추가</Button>
							</div>
						</div>
					</div>
				}
				<ul className="flex flex-col gap-2">
					{
						props.data.length == 0
							&& <div className="w-full text-center font-bold pt-4 pb-2">등록된 메모가 존재하지 않습니다.</div>
					}
					{
						props.data.map((memo, index) => (
							<li key={memo.id} className="bg-white p-2 border-2 rounded-md">
								<div className="flex justify-between items-center">
									<div className="flex items-center gap-2">
										<div className="font-bold text-lg">{memo.memberDepartment} {memo.memberName} 프로</div>
										{
											(
												memo.memberId === authStore.id
												|| (authStore.authLevel && authStore.authLevel <= 2)
											) &&
											<button onClick={() => { props.onDelete(memo.id); } }>
												<HiXMark className="text-red-500" />
											</button>
										}
									</div>
									<div className="text-gray-700 text-sm">({new Date(memo.createdAt).toLocaleDateString()})</div>
								</div>
								<div className="whitespace-pre-wrap">{memo.content}</div>
							</li>
						))
					}
				</ul>
			</div>
		</Card>
	)
}

export default MemoSection;