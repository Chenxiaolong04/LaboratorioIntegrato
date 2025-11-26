import { type FormEvent, type ChangeEvent, useState } from "react";
import { FaSearch } from "react-icons/fa";
import Input from "./Input";


interface SearchBarProps {
  placeholder?: string;
  onSearch: (query: string) => void;
  className?: string;
}

export default function SearchBar({
  placeholder = "Cerca...",
  onSearch,
  className = "",
}: SearchBarProps) {
  const [query, setQuery] = useState("");

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    onSearch(query.trim());
  };

  return (
    <form className={`search-bar ${className}`} onSubmit={handleSubmit}>
      <Input
        name="searchbar"
        type="text"
        value={query}
        onChange={handleChange}
        placeholder={placeholder}
      />
      <button type="submit" className="search-btn">
        <FaSearch size={24}/>
      </button>
    </form>
  );
}
