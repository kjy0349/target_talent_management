import {Textarea, TextareaProps} from "flowbite-react";

interface CustomTextareaProps extends TextareaProps {
	value?: string;
}

const CustomTextarea = (props: CustomTextareaProps) => {

	return (
		<div className={ "relative " + props.className }>
			<Textarea
				className="resize-none w-full"
				rows={props.rows}
				placeholder={props.placeholder}
				value={props.value}
				onChange={props.onChange}
				maxLength={props.maxLength}
			/>
			<div className="w-full text-right mt-1 pr-1">
				<span className="text-blue-500">{ props.value?.length }</span>
				<span>/</span>
				<span>{ props.maxLength }</span>
			</div>
		</div>
	);
};

export default CustomTextarea;