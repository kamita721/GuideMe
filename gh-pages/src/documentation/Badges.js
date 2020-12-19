import React from "react";

export function RequiredBadge(props) {
  return (props.required
      ? <span className="badge d-inline badge-success" aria-label="Required" title="Required">Required</span>
      : <span className="badge d-inline badge-secondary" aria-label="Optional" title="Optional">Optional</span>
  );
}

export function MinVersionBadge(props) {
  return(props.version
      ? <span className={props.className || "badge badge-info d-inline ml-2 mb-1"}
              aria-label={"Requires Version " + props.version.toString()}
              title={"Requires Version " + props.version.toString()}>&gt;= {props.version}</span>
      : ""
  );
}

export function DeprecatedBadge(props) {
  return(props.deprecated
      ? <span className={props.className || "badge badge-warning d-inline ml-2 mb-1"}
              aria-label="Deprecated"
              title="Deprecated">Deprecated</span>
      : ""
  );
}

export function RemovedBadge(props) {
  return(props.version
      ? <span className={props.className || "badge badge-danger d-inline ml-2 mb-1"}
              aria-label={"Removed in Version " + props.version.toString()}
              title={"Removed in Version " + props.version.toString()}>&lt; {props.version}</span>
      : ""
  );
}
