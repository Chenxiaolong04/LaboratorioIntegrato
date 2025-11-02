interface LabelProps {
  text: string;
  className?: string;
}

export default function Label({ text, className }: LabelProps) {
  return (
    <label className={className}>
      {text}
    </label>
  );
}
